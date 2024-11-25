import request from '@/utils/request'

// 网络列表
export function logList(data) {
    return request({
        url: 'log/findList',
        method: 'post',
        data
    })
}
