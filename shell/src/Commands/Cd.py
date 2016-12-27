import os
from src.Commands.BaseCommand import Command


class Cd(Command):
    def __init__(self, arguments:str):
        self.arguments = arguments

    def execute(self, shell_state):
        if self.arguments:
            dir_name = self.arguments
            os.chdir(dir_name)
            shell_state.cur_result = os.getcwd()