import tempfile
import unittest

from termcolor import colored

from src.Commands.Grep import Grep
from src.Main import State


class TestGrep(unittest.TestCase):
    def setUp(self):
        self.state = State()
        self.variables = {}
        self.difficult_test = 'Sun is good!\nYe!1\nYe!2\nSun is such a good thing\nY1\nY2\nY3\nY4\nEvbdy loves sun\nP'
        self.ans_for_difficult = colored('Sun', 'red') + \
                         ' is good!\nYe!1\nYe!2\n' + colored('Sun', 'red') + \
                         ' is such a good thing\nY1\nY2\nY3\nEvbdy loves ' + colored('sun', 'red') + '\nP'

    def test_assignment(self):
        test1 = " w"
        test2 = " -i w"

        g1 = Grep(test1)
        g2 = Grep(test2)

        self.assertEqual(g1.args.pattern, 'w')
        self.assertFalse(g1.args.i)
        self.assertEqual(g1.args.A, 0)
        self.assertEqual(g1.args.file, None)

        self.assertEqual(g2.args.pattern, 'w')
        self.assertTrue(g2.args.i)
        self.assertEqual(g1.args.A, 0)
        self.assertEqual(g1.args.file, None)

    def test_execute(self):
        self.state.cur_result = 'HW'
        test1 = '-i w'
        g1 = Grep(test1)
        g1.execute(self.state)

        self.assertEqual(self.state.cur_result, 'H' + colored('W', 'red'))
        self.state.cur_result = 'I have a sandwich\nIt is a sandwich.'
        test2 = 'sandw'
        g2 = Grep(test2)
        g2.execute(self.state)

        self.assertEqual(self.state.cur_result, 'I have a ' + colored('sandw', 'red') + 'ich\nIt is a ' +
                         colored('sandw', 'red') + 'ich.')

        test3 = 'sandw -w'
        self.state.cur_result = 'I have a sandwich\nIt is a sandwich.'
        g3 = Grep(test3)
        g3.execute(self.state)

        self.assertEqual(self.state.cur_result, '')

        test4 = 'sandwich -w'
        self.state.cur_result = 'I have a sandwich\nIt is a sandwich.'
        g4 = Grep(test4)
        g4.execute(self.state)

        self.assertEqual(self.state.cur_result, 'I have a ' + colored('sandwich', 'red') +
                         '\nIt is a ' + colored('sandwich', 'red') + '.')

        test5 = 'sun -w -i -A 3'
        self.state.cur_result = self.difficult_test
        g5 = Grep(test5)
        g5.execute(self.state)

        self.assertEqual(self.state.cur_result, self.ans_for_difficult)

    def test_execute_with_file(self):
        contains = self.difficult_test
        tmp = tempfile.NamedTemporaryFile(mode='w')
        tmp.write(contains)
        tmp.flush()
        test = 'sun ' + tmp.name + ' -w -i -A 3'
        g = Grep(test)
        g.execute(self.state)
        self.assertEqual(self.state.cur_result, self.ans_for_difficult)
        tmp.file.close()
        return tmp

