import os
from src.Commands.BaseCommand import Command


class Ls(Command):
    def execute(self, shell_state):
        current_dir = os.getcwd()
        shell_state.cur_result = os.listdir(current_dir)