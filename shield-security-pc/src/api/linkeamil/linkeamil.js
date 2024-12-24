import request from '@/utils/request'

//联系邮箱列表
export function linkEmailGetList(data) {
    return request({
        url: 'linkEmail/findList',
        method: 'post',
        data
    })
}


// 新增
export function linkEmailAdd(data) {
    return request({
        url: 'linkEmail/add',
        method: 'post',
        data: data
    })
}

// 编辑邮箱
export function linkEmailUpdate(data) {
    return request({
        url: 'linkEmail/update',
        method: 'post',
        data: data
    })
}

export function LinkEmailDelete(id) {
    return request({
        url: 'linkEmail/delete',
        method: 'get',
        params: {
            'id': id
        }
    })
}
