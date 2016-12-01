from src.Commands.BaseCommand import Command
from src.ParserUtils import read_word, count_of_spaces, read_argument


# Command, that means assignment, it changes current Environment.

class Assignment(Command):
    def __init__(self, arguments:str):  # Initialize arguments, means "variable = value"
        self.variable = read_word(arguments) # read_word - read a word from string, i.e.
                                          # read till the current char is valid(valid chars you can see in Utils).
        # variable can be only a word.
        cur_position = len(self.variable)
        cur_position += count_of_spaces(arguments[cur_position:]) # return count of spaces at he beginning
        # of argument string
        if arguments[cur_position] != '=':
            # We are supposed to find '=', but if we don't find - we throw exception.
            raise NameError("Unknown command: " + arguments)
        cur_position += 1
        self.value = read_argument(arguments[cur_position:]) # read till char is not white space.
        # If there are more then variable and value, we ignore it. It isn't a syntax error.

    def execute(self, shell_state):     # Execute and change Environment. That means add or replace value
                                        # for variable in shell_state. This command hasn't influence on the
        # !!        print(self.variable, self.value)
        shell_state.cur_result = ""  # cur_result in shell_state saves current result to transfer through pipe or
                                     # to print.
        shell_state.add_variable(self.variable, self.value)  # it changes or replaces variable in environment
