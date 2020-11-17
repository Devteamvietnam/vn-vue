import JSEncrypt from 'jsencrypt/bin/jsencrypt'

// Key pair generation http://web.chacuo.net/netrsakeypair

const publicKey ='MFwwDQYJKoZIhvcNAQEBBQADSwAwSAJBAKn70cP7Jsd/ucf52Kg/d59A7XzAVU2U\n' +
  'Ui3ACLnd6pVaxLoQwQHcM3nSgWPGrgLKvgw342vwj5E2mrDhhnowuJ0CAwEAAQ=='

const privateKey ='MIIBnjBABgkqhkiG9w0BBQ0wMzAbBgkqhkiG9w0BBQwwDgQIPJwEGaGxGxcCAggA\n' +
  'MBQGCCqGSIb3DQMHBAg5T6q3T+L2zgSCAVhTEz+jx8ZCcJQce4h8+eYs2CQXu/UB\n' +
  'rXJcqR4QZRYBw6cTfdc2iCIsLu3ICo2g7BMt49aLgNu7jIuaF5Yi/YlPAl1VGlNa\n' +
  'DrMFpz1jOXnUokdWprKlBnR89YOdfo3aSuZrcsQqY/uz0IheKOgd+ARTRJw11mPa\n' +
  'b7jLG2qU6L0ILnHd+WxQZnbxskI+BUQD2CtPOEMTQ4p499+gG5+/XxXIUQx0j+yQ\n' +
  'klkqCoVJCaMLEJSfaYMkBB7u2ngYhHIW6xmMgzr6gs12BrPcckO20WrWDT3B4hY4\n' +
  'lz86ESOzrB4R26AE9JAj9LKaTRO5v6gnWnKi8CbPHd4IGO3cpKjzx5r2iVd87AKJ\n' +
  'hj+OBZGwgJEE54LF7fxiJL+SKr3C/g/lLJbCh6XETIRyNDGLv904H8VPPv3Gt9QZ\n' +
  'uWQB9BGxuSuC7fTDp9L4RNVNaBTK31VuP/rmTS2dAoErxQ=='

// encryption
export function encrypt(txt) {
  const encryptor = new JSEncrypt()
  encryptor.setPublicKey(publicKey) // set public key
  return encryptor.encrypt(txt) // encrypt the data
}

// decrypt
export function decrypt(txt) {
  const encryptor = new JSEncrypt()
  encryptor.setPrivateKey(privateKey) // set private key
  return encryptor.decrypt(txt) // Decrypt the data
}
