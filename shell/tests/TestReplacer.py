from unittest import TestCase

from src.Main import State
from src.Replacer import substitute


class TestReplacer(TestCase):
    def test_substitute(self):
        state = State()
        state.add_variable('a', '1')
        state.add_variable('b', 'c')
        state.add_variable('q', '*')

        line1 = '$a place is gained by $q. "$b"'
        subst_positions = [0, 22, 27]
        res_line = substitute(line1, subst_positions, state)
        self.assertEqual(res_line, '1 place is gained by *. "c"')
