import random
import unittest

from src.Commands.Echo import Echo
from src.Main import State


class TestEcho(unittest.TestCase):
    def setUp(self):
        self.state = State()
        self.variables = {}

    def test_echo(self):
        cnt = 5
        for i in range(cnt):
            a = random.randrange(10)
            e = Echo(a)
            self.assertEqual(e.arguments, a)
            e.execute(self.state)
            self.assertEqual(self.variables, self.state.variables)
            self.assertEqual(a, self.state.cur_result)
