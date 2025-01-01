import request from '@/utils/request'

// 新增app网络配置
export function appConfigAdd(data) {
    return request({
        url: 'appConfig/add',
        method: 'post',
        data
    })
}

//编辑app网络配置
export function appConfigUpdate(data) {
    return request({
        url: 'appConfig/update',
        method: 'post',
        data
    })
}

//app网络配置列表
export function appConfiglist(data) {
    return request({
        url: 'appConfig/findList',
        method: 'post',
        data
    })
}
