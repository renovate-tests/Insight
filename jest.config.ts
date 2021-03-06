import type { Config } from '@jest/types';

const config: Config.InitialOptions = {
  roots: ['<rootDir>/src'],
  transform: {
    '.ts': 'ts-jest',
  },
  testEnvironment: 'node',
  coveragePathIgnorePatterns: ['/node_modules/'],
  moduleDirectories: ['node_modules', 'src'],
};

export default config;
