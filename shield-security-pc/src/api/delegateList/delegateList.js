import request from '@/utils/request'

export function findDelegatelist(data) {
    return request({
        url: 'delegatelist/findList',
        method: 'post',
        data
    })
}
