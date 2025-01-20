import request from '@/utils/request'

// 新增联系方式
export function discoverStateList() {
    return request({
        url: 'discoverState/getList',
        method: 'get',
    })
}

//编辑联系方式
export function discoverStateUpdate(states) {
    return request({
        url: 'discoverState/update',
        method: 'get',
        params: {
            'states': states
        }
    })
}
