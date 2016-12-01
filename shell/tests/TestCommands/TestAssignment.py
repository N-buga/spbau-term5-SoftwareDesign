import unittest

from src.Commands.Assignment import Assignment
from src.Main import State


class TestAssignment(unittest.TestCase):
    def setUp(self):
        self.state = State()
        self.variables = {}

    def test_assignment(self):
        test_data = [("1", "ava"), ("one", "two")]

        a = []
        for tpl in test_data:
            cur_a = Assignment(tpl[0] + "=" + tpl[1])
            self.assertEqual((cur_a.variable, cur_a.value), tpl)
            a.append(cur_a)

        for i in range(len(test_data)):
            a[i].execute(self.state)
            self.variables[test_data[i][0]] = test_data[i][1]
            self.assertEqual(self.state.cur_result, "")
            self.assertEqual(self.state.variables, self.variables)