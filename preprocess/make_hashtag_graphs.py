#!/usr/bin/env python


import codecs
import json
import re


WHITESPACE_RE = re.compile(r'\s+')
NON_WORD_RE = re.compile(r'([^\w]|_)+')
BAD_WORD_TEMPLATE = '_FILTERED_%d_'


class NameCleaner(object):
    def __init__(self, profanity_filename):
        self.profane_words_map = dict()

        profanity = set()
        with codecs.open(profanity_filename, encoding='utf-8') as f:
            for line in f:
                profanity.add(line.strip())
        self.profanity_re = re.compile('(' + '|'.join(profanity) + ')')
        
    def clean_name(self, name):
        whitespace_stripped_name = WHITESPACE_RE.subn(' ', name)[0]
        word_only_name = NON_WORD_RE.subn('', whitespace_stripped_name)[0]
        if self.profanity_re.search(word_only_name) is not None:
            if whitespace_stripped_name in self.profane_words_map:
                return self.profane_words_map[whitespace_stripped_name]
            else:
                clean_name = BAD_WORD_TEMPLATE % len(self.profane_words_map)
                self.profane_words_map[whitespace_stripped_name] = clean_name
                return clean_name
        else:
            return whitespace_stripped_name


def update_counts(counts_dict, key):
    if key in counts_dict:
        counts_dict[key] += 1
    else:
        counts_dict[key] = 1


def update_counts_2d(counts_dict, key1, key2):
    if key1 in counts_dict:
        update_counts(counts_dict[key1], key2)
    else:
        counts_dict[key1] = {key2: 1}


def count_undirected_pairs(category_item_counts):
    pair_counts = dict()
    for (category, item_counts) in category_item_counts.items():
        items = item_counts.keys()
        for i1 in xrange(len(items)):
            for i2 in xrange(i1+1, len(items)):
                item1 = items[i1]
                item2 = items[i2]
                if item1 < item2:
                    update_counts(pair_counts, (item1, item2))
                else:
                    update_counts(pair_counts, (item2, item1))
    return pair_counts


def write_graph(filename, edge_counts):
    with codecs.open(filename, encoding='utf-8', mode='w') as f:
        for (edge, count) in edge_counts.items():
            f.write(u'%s\t%s\t%d\n' % (edge[0], edge[1], count))


def main(profanity_filename, in_filename, hashtag_edge_out_filename,
        user_edge_out_filename):
    hashtag_name_cleaner = NameCleaner(profanity_filename)
    user_name_cleaner = NameCleaner(profanity_filename)

    hashtag_user_counts = dict()
    user_hashtag_counts = dict()

    with codecs.open(in_filename, encoding='utf-8') as in_f:
        for line in in_f:
            j = json.loads(line)
            if 'user' in j:
                username = user_name_cleaner.clean_name(
                    j['user']['screen_name'])
                for hashtag_dict in j['entities']['hashtags']:
                    hashtag = hashtag_name_cleaner.clean_name(
                        hashtag_dict['text'].lower())
                    update_counts_2d(hashtag_user_counts, hashtag, username)
                    update_counts_2d(user_hashtag_counts, username, hashtag)

    hashtag_edge_counts = count_undirected_pairs(hashtag_user_counts)
    user_edge_counts = count_undirected_pairs(user_hashtag_counts)

    write_graph(hashtag_edge_out_filename, hashtag_edge_counts)
    write_graph(user_edge_out_filename, user_edge_counts)


if __name__ == '__main__':
    import sys
    main(*sys.argv[1:])
