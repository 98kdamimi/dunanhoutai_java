import request from '@/utils/request'

// 新增帮助菜单
export function helpAdd(data) {
    return request({
        url: 'help/add',
        method: 'post',
        data
    })
}

//编辑帮助菜单
export function helpUpdate(data) {
    return request({
        url: 'help/update',
        method: 'post',
        data
    })
}

//帮助菜单列表
export function helplist(data) {
    return request({
        url: 'help/getList',
        method: 'post',
        data
    })
}

export function helpDelete(id) {
    return request({
        url: 'help/delete',
        method: 'get',
        params: {
            'id': id
        }
    })
}
