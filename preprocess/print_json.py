#!/usr/bin/env python


import codecs
import json


def print_json(filename):
    with codecs.open(filename, encoding='utf-8') as f:
        for line in f:
            j = json.loads(line)
            if 'user' in j:
                json_str = json.dumps(j,
                    indent=4, ensure_ascii=False, encoding='utf-8') + u'\n'
                sys.stdout.write(json_str.encode('utf-8'))


if __name__ == '__main__':
    import sys
    print_json(*sys.argv[1:])
