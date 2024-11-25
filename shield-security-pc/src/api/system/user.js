import request from '@/utils/request'
import { praseStrEmpty } from "@/utils/ruoyi";

// 查询用户列表
export function listUser(data) {
    return request({
        url: 'user/findList',
        method: 'post',
        data
    })
}

// 查询用户详细
export function getUser(userId) {
    return request({
        url: '/system/user/' + praseStrEmpty(userId),
        method: 'get'
    })
}

// 新增用户
export function addUser(data) {
    return request({
        url: '/user/signUp',
        method: 'post',
        headers: {
            'Content-Type': 'multipart/form-data' //注意这里
        },
        data: data
    })
}

// 修改用户
export function updateUser(data) {
    return request({
        url: 'user/updateUser',
        method: 'post',
        headers: {
            'Content-Type': 'multipart/form-data' //注意这里
        },
        data: data
    })
}

export function delUser(userId) {
    return request({
        url: 'user/delUser/' + userId,
        method: 'delete'
    })
}

// 修改用户密码
export function resetPwd(userId, password) {
    return request({
        url: '/user/resetPwd',
        method: 'post',
        params: {
            'userId': userId,
            'password': password
        }
    })
}
// 用户密码重置
export function resetUserPwd(userId) {
    return request({
        url: '/user/resetUserPwd',
        method: 'post',
        params: {
            'userId': userId,
        }
    })
}

// 用户状态修改
export function changeUserStatus(userId, status) {
    const data = {
        userId,
        status
    }
    return request({
        url: '/system/user/changeStatus',
        method: 'put',
        data: data
    })
}

// 查询用户个人信息
export function getUserProfile() {
    return request({
        url: '/system/user/profile',
        method: 'get'
    })
}

// 修改用户个人信息
export function updateUserProfile(data) {
    return request({
        url: '/system/user/profile',
        method: 'put',
        data: data
    })
}

// 用户密码重置
export function updateUserPwd(oldPassword, newPassword) {
    const data = {
        oldPassword,
        newPassword
    }
    return request({
        url: '/system/user/profile/updatePwd',
        method: 'put',
        params: data
    })
}

// 用户头像上传
export function uploadAvatar(data) {
    return request({
        url: '/system/user/profile/avatar',
        method: 'post',
        data: data
    })
}

// 查询授权角色
export function getAuthRole(userId) {
    return request({
        url: '/system/user/authRole/' + userId,
        method: 'get'
    })
}

// 保存授权角色
export function updateAuthRole(data) {
    return request({
        url: '/system/user/authRole',
        method: 'put',
        params: data
    })
}

// 校验账号密码
export function verifyPwd(userId, password) {
    return request({
        url: '/user/verifyPwd',
        method: 'post',
        params: {
            'userId': userId,
            'password': password
        }
    })
}

export function findUserLower(userId) {
    return request({
        url: '/user/findUserLower',
        method: 'get',
        params: {
            'userId': userId,
        }
    })
}


export function generateToken() {
    return request({
        url: '/user/generateToken',
        method: 'get',
    })
}
