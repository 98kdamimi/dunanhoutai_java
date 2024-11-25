import router from './router'
import store from './store'
import { Message } from 'element-ui'
import NProgress from 'nprogress'
import 'nprogress/nprogress.css'
import { getToken, removeToken } from '@/utils/auth'
import { isRelogin } from '@/utils/request'
import auth from "@/plugins/auth";

NProgress.configure({ showSpinner: false })
    // '/', '/index', '/inspectionRecords'
const whiteList = ['/login', '/register', ]

router.beforeEach((to, from, next) => {
    NProgress.start()
    if (getToken()) {
        to.meta.title && store.dispatch('settings/setTitle', to.meta.title)
            /* has token*/
        if (to.path === '/login') {
            next({ path: '/' })
            NProgress.done()
        } else {
            if (store.getters.roles.length === 0) {
                // console.log("1111111111");
                // isRelogin.show = true
                // 判断当前用户是否已拉取完user_info信息
                store.dispatch('GetInfo').then(() => {
                    if (to.path !== '/login') {
                        let roleFlag = auth.hasRole('admin') || auth.hasRole('mshengban') || auth.hasRole('guanli') || auth.hasRole('root')
                        if (!roleFlag) {
                            Message({
                                message: '您没有登录权限，请联系管理员',
                                type: 'error',
                                duration: 5 * 1000
                            })
                            store.commit('SET_TOKEN', '')
                            store.commit('SET_ROLES', [])
                            store.commit('SET_PERMISSIONS', [])
                            removeToken()
                            NProgress.done()
                            return
                        }
                    }
                    // isRelogin.show = false
                    store.dispatch('GenerateRoutes').then(accessRoutes => {
                        // 根据roles权限生成可访问的路由表
                        router.addRoutes(accessRoutes) // 动态添加可访问路由表
                        next({...to, replace: true }) // hack方法 确保addRoutes已完成
                    })

                }).catch(err => {
                    // console.log("333333333", err);

                    store.dispatch('LogOut').then(() => {
                        Message.error(err)
                        next({ path: '/' })
                    })
                })
            } else {

                next()
            }
        }
    } else {
        // 没有token
        if (whiteList.indexOf(to.path) !== -1) {
            // 在免登录白名单，直接进入
            next()
        } else {
            next(`/login?redirect=${encodeURIComponent(to.fullPath)}`) // 否则全部重定向到登录页
            NProgress.done()
        }
    }
})

router.afterEach(() => {
    NProgress.done()
})