import os
from src.Commands.BaseCommand import Command


# Class for ls command. It takes zero arguments and show user all files and directories in current directory
class Ls(Command):
    def execute(self, shell_state):
        # Get current directory
        current_dir = os.getcwd()
        # Show all directory's content
        shell_state.cur_result = os.listdir(current_dir)