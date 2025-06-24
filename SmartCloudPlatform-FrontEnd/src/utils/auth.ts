const TOKEN_KEY = 'smart_cloud_token'
const USER_INFO_KEY = 'smart_cloud_user_info'

// 获取token
export function getToken(): string | null {
  return localStorage.getItem(TOKEN_KEY)
}

// 设置token
export function setToken(token: string): void {
  localStorage.setItem(TOKEN_KEY, token)
}

// 移除token
export function removeToken(): void {
  localStorage.removeItem(TOKEN_KEY)
}

// 获取用户信息
export function getUserInfo(): any {
  const userInfo = localStorage.getItem(USER_INFO_KEY)
  return userInfo ? JSON.parse(userInfo) : null
}

// 设置用户信息
export function setUserInfo(userInfo: any): void {
  localStorage.setItem(USER_INFO_KEY, JSON.stringify(userInfo))
}

// 移除用户信息
export function removeUserInfo(): void {
  localStorage.removeItem(USER_INFO_KEY)
}

// 清除所有认证信息
export function clearAuth(): void {
  removeToken()
  removeUserInfo()
}

// 检查是否已登录
export function isLoggedIn(): boolean {
  return !!getToken()
} 