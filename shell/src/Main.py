from src import ShellParser


class State:  # Current state of shell
    def __init__(self):
        self.variables = {}  # Environment
        self.cur_result = ""  # Current data in Pipe
        self.is_commands_before = False  # If there wasn't commands before and we've just open shell or not.

    def add_variable(self, var, value):  # setter for Environment
        self.variables[var] = value

    def find_variable(self, var):  # getter for Environment
        if var in self.variables:
            return self.variables[var]


if __name__ == "__main__":
    state = State()
    while True:  # Read line by line
        line = input()
        if line == "":
            continue
        try:
            commands = ShellParser.parse(line, state)  # Parse line to get a list of Command, it's necessary
            # to have environment to parse, so we give it State.
        except NameError as e:  # Processing Errors from parse.
            print(e)
            continue
        except SyntaxError as e:
            print(e)
            continue

        try:
            for command in commands:
                    command.execute(state)
        except NameError as e:  # ProcessingError from execute
            print(e)
            continue

        print(state.cur_result)
