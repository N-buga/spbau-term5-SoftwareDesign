import unittest

from src.Commands.Cd import Cd
from src.Main import State


class TestCd(unittest.TestCase):
    def setUp(self):
        self.state = State()
        self.variables = {}

    def test_cd(self):
        directory = "../../"
        cd = Cd(directory)

        self.assertEqual(directory, cd.arguments)

        cd.execute(self.state)
        self.assertEqual(self.state.variables, {})
        self.assertEqual(self.state.cur_result, 'C:\\Users\Admin\Anastasia_documents\AU\SoftwareDesign\\task3'
                                                '\spbau-term5-SoftwareDesign\shell')


