import request from '@/utils/request'

export function findDelegatelist(data) {
    return request({
        url: 'delegatelist/findList',
        method: 'post',
        data
    })
}


export function findStatistics(data) {
    return request({
        url: 'delegatelist/findStatistics',
        method: 'post',
        data
    })
}

export function findStatisticsNum(data) {
    return request({
        url: 'delegatelist/findStatisticsNum',
        method: 'post',
        data
    })
}

