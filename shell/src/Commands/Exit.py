import sys

from src.Commands.BaseCommand import Command

# Exit script.


class Exit(Command):
    def execute(self, shell_state):
        print("exit_")
        sys.exit(0)