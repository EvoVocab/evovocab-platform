const os = require('os');
const path = require('path');
const { execSync } = require('child_process');

const resolvePath = (p) => {
  if (!p) return '';
  if (p.startsWith('~')) {
    return path.join(os.homedir(), p.slice(1));
  }
  return path.resolve(p);
};

const runCmd = (cmd, inputData = null) => {
  try {
    const options = { stdio: inputData ? ['pipe', 'inherit', 'inherit'] : 'inherit' };
    if (inputData) options.input = inputData;
    return execSync(cmd, options);
  } catch (error) {
    console.error(`\n❌ Lỗi khi chạy lệnh: ${cmd}`);
    process.exit(1);
  }
};

const runCmdSilent = (cmd) => {
  try {
    return execSync(cmd, { stdio: 'pipe' }).toString();
  } catch (error) {
    return null;
  }
};

const rootDir = path.resolve(__dirname, '../');

module.exports = { rootDir, resolvePath, runCmd, runCmdSilent };
