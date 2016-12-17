from unittest import TestCase

from src.Commands.Assignment import Assignment
from src.Commands.Cat import Cat
from src.Commands.Echo import Echo
from src.Commands.Executable import Executable
from src.Commands.Exit import Exit
from src.Commands.Pwd import Pwd
from src.Commands.Wc import Wc
from src.CommandsFactory import create_command


class TestCommandsFactory(TestCase):
    def test_create_command_exit(self):
        cmd = create_command('exit', 'args')
        self.assertTrue(isinstance(cmd, Exit))

    def test_create_command_assignment(self):
        cmd = create_command('assignment', 'a=2')
        self.assertTrue(isinstance(cmd, Assignment))
        self.assertEqual(cmd.variable, 'a')
        self.assertEqual(cmd.value, '2')
        with self.assertRaises(NameError):
            create_command('assignment', 'a b')

    def test_create_command_executable(self):
        script_name = '../exec.sh'
        argument = ' 4'
        cmd = create_command('executable', script_name + argument)
        self.assertTrue(isinstance(cmd, Executable))
        self.assertEqual(cmd.script_name, script_name)
        self.assertEqual(cmd.arguments, argument)

        file_name = './__init__.py'
        with self.assertRaises(NameError):
            create_command('executable', file_name)

    def test_create_command_cat(self):
        cmd = create_command('cat', '../exec.sh  a c')
        self.assertTrue(isinstance(cmd, Cat))
        self.assertEqual(cmd.arguments, ['../exec.sh', 'a', 'c'])

    def test_create_command_echo(self):
        s = 'adkn sdh fgygf 11'
        cmd = create_command('echo', s)
        self.assertTrue(isinstance(cmd, Echo))
        self.assertEqual(cmd.arguments, s)

    def test_create_command_pwd(self):
        cmd = create_command('pwd', 'sfjsldkj')
        self.assertTrue(cmd, Pwd)

    def test_create_command_wc(self):
        cmd = create_command('wc', 'a b c d')
        self.assertTrue(cmd, Wc)
        self.assertEqual(cmd.arguments, ['a', 'b', 'c', 'd'])