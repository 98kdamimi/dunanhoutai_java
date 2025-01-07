import request from '@/utils/request'

// 设备注册数量统计
export function getEquipment() {
    return request({
        url: 'statistics/equipment',
        method: 'get'
    })
}

//设备注册统计图表
export function getEquipmentChar(data) {
    return request({
        url: 'statistics/equipmentChar',
        method: 'post',
        data
    })
}

//账号数量统计
export function getAccount() {
    return request({
        url: 'statistics/account',
        method: 'get'
    })
}

export function getaAccountChar(data) {
    return request({
        url: 'statistics/accountChar',
        method: 'post',
        data
    })
}
