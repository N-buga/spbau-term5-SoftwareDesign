import argparse
import os
import re

import io
from termcolor import colored

import sys as _sys

from src import ParserUtils
from src.Commands.BaseCommand import Command


# This class is for to not exit after error in parsing. So we use it as parser class.

class ThrowingArgumentParser(argparse.ArgumentParser):
    def exit(self, status=0, message=None):
        if message:
            self._print_message(message, _sys.stderr)  # print the error massage and work further.
        pass


# Grep finds all matching some pattern in file or data from pipe.

class Grep(Command):
    def __init__(self, string_arguments:str):
        # initialization available keys
        parser = ThrowingArgumentParser(add_help=True, prog='grep', description='search the pattern in text')
        parser.add_argument('-i', action='store_true', help='ignore register')
        parser.add_argument('-w', action='store_true', help='only whole word')
        parser.add_argument('-A', type=int, metavar='N', action='store', default=0, help='count of ' +
                                                                                         'lines printed after the line with pattern')
        parser.add_argument('pattern', help='pattern that is searching in text')
        parser.add_argument('file', nargs='?', help='file where will be look for the pattern in')
        try:
            self.args = parser.parse_args(string_arguments.split())
        except:
            raise NameError("grep: Wrong arguments")
        self.args.pattern = ParserUtils.delete_quotes(self.args.pattern)
        self.args.file = ParserUtils.delete_quotes(self.args.file)

    def execute(self, shell_state):
        if self.args.w:
            self.args.pattern = '\\b' + self.args.pattern + '\\b'  # this matches the pattern as the whole word
        if self.args.file is None:
            text = shell_state.cur_result
            # for unification work with text from pipe and file I use stream instead of string
            stream = io.StringIO(text)
            shell_state.cur_result = self.__sort_out_lines(stream)  # it sorts out all lines and find the pattern there
            stream.close()
        else:
            if not os.path.exists(self.args.file):
                raise NameError(
                    "Grep. File " + "'" + self.args.file + "'" + " doesn't find")  # Something goes wrong. Very bad.
                # We raise Exception to handle it in Main.
            try:
                with open(self.args.file,
                          'r') as fin:  # We open file in with so it closes automatically in the end of cycle.
                    shell_state.cur_result = self.__sort_out_lines(fin)
            except IsADirectoryError as e:
                raise NameError('Grep. File ' + "'" + self.args.file + "'" + " is a directory.")

    # it sorts out all lines and find the pattern there

    def __sort_out_lines(self, iterable):
        ans = ''
        cnt_after_line = self.args.A + 1
        for line in iterable:
            try:
                if self.args.i:
                    iter = re.finditer(self.args.pattern, line, re.I)
                else:
                    iter = re.finditer(self.args.pattern, line)
            except:
                raise NameError("Wrong pattern")
            prev_ind = 0
            for i in iter:
                ans += line[prev_ind:i.start()]
                ans += colored(line[i.start():i.end()], 'red')
                prev_ind = i.end()
                cnt_after_line = 0
            if cnt_after_line <= self.args.A:
                ans += line[prev_ind:]
                cnt_after_line += 1
        return ans
