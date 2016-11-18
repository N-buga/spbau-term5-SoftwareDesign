from src.Commands.BaseCommand import Command


# Command, that means assignment, it changes current Environment.


class Assignment(Command):
    def __init__(self, variable="", value=""):  # Initialize arguments, means "variable = value"
        self.variable = variable
        self.value = value

    def execute(self, shell_state):     # Execute and change Environment. That means add or replace value
                                        # for variable in shell_state. This command hasn't influence on the
        # !!        print(self.variable, self.value)
        shell_state.cur_result = ""  # cur_result in shell_state saves current result to transfer through pipe or
                                     # to print.
        shell_state.add_variable(self.variable, self.value)  # it changes or replaces variable in environment
