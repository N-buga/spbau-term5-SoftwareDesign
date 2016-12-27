import os
from src.Commands.BaseCommand import Command


# Cd command takes directory's name for an argument and change current directory to given
class Cd(Command):
    def __init__(self, arguments: str):
        self.arguments = arguments

    def execute(self, shell_state):
        if self.arguments:
            # Resolve name of the new directory
            dir_name = self.arguments
            # Change directory
            os.chdir(dir_name)
            # Get new directory's name
            shell_state.cur_result = os.getcwd()
