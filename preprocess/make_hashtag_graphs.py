#!/usr/bin/env python


import codecs
import json
import re


WHITESPACE_RE = re.compile(r'\s+')
NON_WORD_RE = re.compile(r'([^\w]|_)+')
BAD_WORD_TEMPLATE = '_CENSORED_%d_'

TWEET_PRINT_INTERVAL = 10000


class Tweet(object):
    def __init__(self, name_cleaner, j):
        self.username = name_cleaner.clean_name(j['user']['screen_name'])
        self.hashtags = set(
                [name_cleaner.clean_name(hashtag_dict['text'].lower())
                for hashtag_dict in j['entities']['hashtags']]
            )

    def username_generator(self):
        yield self.username

    def hashtag_generator(self):
        for hashtag in self.hashtags:
            yield hashtag


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


def make_category_item_counts(tweets, category_generator, item_generator):
    category_item_counts = dict()
    for tweet in tweets:
        for category in category_generator(tweet):
            for item in item_generator(tweet):
                update_counts_2d(category_item_counts, category, item)
    return category_item_counts


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


def tweet_generator(profanity_filename, *tweet_filenames):
    name_cleaner = NameCleaner(profanity_filename)
    for tweet_filename in tweet_filenames:
        print 'Loading tweets from %s...' % tweet_filename
        i = 0
        with codecs.open(tweet_filename, encoding='utf-8') as in_f:
            for line in in_f:
                if i % TWEET_PRINT_INTERVAL == 0:
                    print 'Loading tweet %d...' % i
                j = json.loads(line)
                if 'user' in j:
                    yield Tweet(name_cleaner, j)
                i += 1
        print 'Loaded %d tweets from %s' % (i, tweet_filename)


def write_graph(filename, edge_counts):
    with codecs.open(filename, encoding='utf-8', mode='w') as f:
        for (edge, count) in edge_counts.items():
            f.write(u'%s\t%s\t%d\n' % (edge[0], edge[1], count))


def make_hashtag_per_tweet_graph(profanity_filename,
        hashtag_per_tweet_edge_out_filename, *in_filenames):
    print 'Generating and writing per-tweet hashtag-edge graph...'
    write_graph(hashtag_per_tweet_edge_out_filename, count_undirected_pairs(
        dict([
            (
                tweet,
                dict([
                    (hashtag, 1) for hashtag in tweet.hashtag_generator()
                ])
            )
            for tweet in tweet_generator(profanity_filename, *in_filenames)
        ])))


def make_hashtag_graphs(profanity_filename, hashtag_edge_out_filename,
        user_edge_out_filename, *in_filenames):
    print 'Generating and writing hashtag-edge graph...'
    write_graph(user_edge_out_filename,
        count_undirected_pairs(make_category_item_counts(
            tweet_generator(profanity_filename, *in_filenames),
            category_generator=Tweet.username_generator,
            item_generator=Tweet.hashtag_generator)))
    print 'Generating and writing user-edge graph...'
    write_graph(hashtag_edge_out_filename,
        count_undirected_pairs(make_category_item_counts(
            tweet_generator(profanity_filename, *in_filenames),
            category_generator=Tweet.hashtag_generator,
            item_generator=Tweet.username_generator)))


if __name__ == '__main__':
    import sys
    if len(sys.argv) == 2 and sys.argv[1] == '--test':
        print 'Testing...'
        import doctest
        doctest.testmod()
    else:
        make_hashtag_graphs(*sys.argv[1:])
