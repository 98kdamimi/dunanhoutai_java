import request from '@/utils/request'

export function walletBalanceList(data) {
    return request({
        url: 'walletBalance/getList',
        method: 'post',
        data
    })
}

export function walletGetNumAll() {
    return request({
        url: 'walletBalance/getNumAll',
        method: 'get'
    })
}

export function walletInfoList(data) {
    return request({
        url: 'walletBalanceInfo/getList',
        method: 'post',
        data
    })
}

export function walletInfoNunAll(data) {
    return request({
        url: 'walletBalanceInfo/getNumAll',
        method: 'post',
        data
    })
}

export function getTotalUsdValueByName(data) {
    return request({
        url: 'walletBalanceInfo/getTotalUsdValueByName',
        method: 'post',
        data
    })
}

