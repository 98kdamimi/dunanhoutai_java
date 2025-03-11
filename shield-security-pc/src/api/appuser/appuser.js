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