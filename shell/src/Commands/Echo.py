from src import ParserUtils
from src.Commands.BaseCommand import Command

# Echo data that goes after it.


class Echo(Command):
    def __init__(self, arguments:str):
        self.arguments = arguments
        if isinstance(self.arguments, str):
            self.arguments = ParserUtils.delete_quotes(self.arguments)

    def execute(self, shell_state):
        if self.arguments is not None:
            shell_state.cur_result = self.arguments  # We save all arguments as result cause echo shows all that goes
            # after.
            # cur_result in shell_state saves current result to transfer it through pipe or to print.
