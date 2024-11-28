import request from '@/utils/request'

// 网络列表
export function findCoingeckoToken(data) {
    return request({
        url: 'coingecko/findCoingeckoToken',
        method: 'post',
        data
    })
}

