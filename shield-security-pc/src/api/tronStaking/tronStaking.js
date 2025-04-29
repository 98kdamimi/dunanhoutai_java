import request from '@/utils/request'

// 新增质押账户
export function addTronStaking(data) {
    return request({
        url: 'tronStaking/add',
        method: 'post',
        data
    })
}

//编辑质押账户
export function findTronStaking(data) {
    return request({
        url: 'tronStaking/find',
        method: 'post',
        data
    })
}

//删除质押账户
export function deleteTronStaking(id) {
    return request({
        url: 'tronStaking/delete',
        method: 'get',
        params: {
            'id': id
        }
    })
}
/********************************************************** */
// 新增委托账户
export function addTronDelegation(data) {
    return request({
        url: 'tronDelegation/add',
        method: 'post',
        data
    })
}

//编辑委托账户
export function findTronDelegation(data) {
    return request({
        url: 'tronDelegation/find',
        method: 'post',
        data
    })
}

//删除委托账户
export function deleteTronDelegation(id) {
    return request({
        url: 'tronDelegation/delete',
        method: 'get',
        params: {
            'id': id
        }
    })
}

