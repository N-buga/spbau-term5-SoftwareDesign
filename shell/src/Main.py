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
    while True:  # Read line by line
        line = input()
        state = State()
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
        for command in commands:
            try:
                command.execute(state)
                print(state.cur_result)
            except NameError as e:  # ProcessingError from execute
                print(e)
