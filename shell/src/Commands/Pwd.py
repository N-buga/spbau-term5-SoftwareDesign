import os

from src.Commands.BaseCommand import Command

# Show current work directory.


class Pwd(Command):
    def execute(self, shell_state):
        shell_state.cur_result = os.getcwd()
        # cur_result in shell_state saves current result to transfer it through pipe or to print.
