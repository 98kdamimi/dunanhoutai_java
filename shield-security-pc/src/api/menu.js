import request from '@/utils/request'


// 获取路由 查询用户菜单
export const getRouters = (userId) => {
    return request({
        url: '/menu/findByUser',
        method: 'post',
        params: {
            "userId": userId
        }
    })
}