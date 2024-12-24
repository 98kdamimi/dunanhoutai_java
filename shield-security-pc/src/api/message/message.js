import request from '@/utils/request'
import { praseStrEmpty } from "@/utils/ruoyi";

// 查询消息列表
export function messageList(data) {
    return request({
        url: 'message/findList',
        method: 'post',
        data
    })
}

// 创建消息
export function messageAdd(data) {
    return request({
        url: 'message/add',
        method: 'post',
        data
    })
}

//删除消息
export function messageDelete(id) {
    return request({
        url: 'message/delete',
        method: 'post',
        params: {
            'id': id
        }
    })
}

//发送消息
export function messagePushZh(id) {
    return request({
        url: 'message/pushZh',
        method: 'get',
        params: {
            'id': id
        }
    })
}

export function messagePushEl(id) {
    return request({
        url: 'message/pushEl',
        method: 'get',
        params: {
            'id': id
        }
    })
}