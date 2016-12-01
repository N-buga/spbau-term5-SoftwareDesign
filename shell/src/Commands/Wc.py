import os

from src.Commands.BaseCommand import Command


# Return count of lines, count of words, count of bytes and name of file through the space.


class Wc(Command):
    def __init__(self, arguments:str):
        self.arguments = arguments

    def execute(self, shell_state):
        if self.arguments:  # if self.arguments not null, i.e. it contains something.
            all_count_lines = 0  # If there are more then one argument(file) then we count some statistic:
            # sum of all lines, words and bytes. These variables will be used as accumulators.
            all_count_words = 0
            all_count_bytes = 0
            result = []  # This will be contain result in string view.
            for argument in self.arguments:  # Sort out all files.
                if not os.path.exists(argument):  # Check if it places in current directory
                    raise NameError("File " + "'" + argument + "'" + " doesn't find")
                    # If not we need to throw exception to hold it int the Main.
                count_words = 0
                count_lines = 0
                # This is words count and lines count for current file.
                with open(argument, 'r') as fin:  # We open file in with so it closes automatically
                    count_lines += 1
                    count_words += len(fin.read().split())
                count_bytes = os.path.getsize(argument)
                result.append(str(count_lines) + ' ' + str(count_words) + ' ' + str(count_bytes) + ' ' + argument)
                all_count_bytes += count_bytes
                all_count_lines += count_lines
                all_count_words += count_words
            if len(self.arguments) > 1:  # If we have more then 1 file to analyze, we show statistics
                result.append(str(all_count_lines) + ' ' + str(all_count_words) + ' ' + str(all_count_bytes) +
                              ' Иотого')
            shell_state.cur_result = '\n'.join(result)
            # cur_result in shell_state saves current result to transfer it through pipe or to print.
        else:
            print(shell_state.cur_result)
            result = [str(len(shell_state.cur_result.split('\n'))), str(len(shell_state.cur_result.split())),
                      str(len(bytearray(shell_state.cur_result.encode())))]
            # it there are no arguments then we take data from pipe, from cur_result in shell_state.
            # After that cur_result changes.
            shell_state.cur_result = ' '.join(result)
            # cur_result in shell_state saves current result to transfer it through pipe or to print.
