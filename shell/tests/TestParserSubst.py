from unittest import TestCase
from src.ParserSubst import find_substitute_positions


class TestParserSubst(TestCase):
    def test_find_substitute_positions(self):
        line1 = '$a $$b lkdlhsd $1'
        res1 = find_substitute_positions(line1)
        self.assertEqual(res1, [0, 4, 15])
        line2 = "$a $ $b '$c' fk $f"
        res2 = find_substitute_positions(line2)
        self.assertEqual(res2, [0, 5, 16])
        line3 = '$a $b "$v" vf $f'
        res3 = find_substitute_positions(line3)
        self.assertEqual(res3, [0, 3, 7, 14])
