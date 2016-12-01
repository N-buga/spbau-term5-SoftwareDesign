import argparse

import sys as _sys

from src.Commands.BaseCommand import Command


class ThrowingArgumentParser(argparse.ArgumentParser):
    def exit(self, status=0, message=None):
        if message:
            self._print_message(message, _sys.stderr)
        pass


class Grep(Command):
    def __init__(self, string_arguments):
        parser = ThrowingArgumentParser(add_help=True, prog='grep', description='search the pattern in text')
        parser.add_argument('-i', action='store_true', help='ignore register')
        parser.add_argument('-w', action='store_true', help='only whole word')
        parser.add_argument('-A', metavar='N', action='store', default=1, help='count of ' +
                                                                             'lines printed after the line with pattern')
        parser.add_argument('pattern', help='pattern that is searching in text')
        self.args = parser.parse_args(string_arguments.split())

    def execute(self, shell_state):
        if self.args.pattern is None:
            self.args.pattern=shell_state.cur_result

