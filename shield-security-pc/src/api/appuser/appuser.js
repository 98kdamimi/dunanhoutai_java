import request from '@/utils/request'

export function findAppUser(data) {
    return request({
        url: 'appUser/findList',
        method: 'post',
        data
    })
}

export function findInfoAppUser(data) {
    return request({
        url: 'appUser/findInfoList',
        method: 'post',
        data
    })
}

export function findWalletList(data) {
    return request({
        url: 'appUser/findWalletList',
        method: 'post',
        data
    })
}

export function findWalletAll(data) {
    return request({
        url: 'appUser/findWalletAll',
        method: 'post',
        data
    })
}