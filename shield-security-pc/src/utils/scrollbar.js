//自定义滚动条
import PerfectScrollbar from 'perfect-scrollbar'
//对应的css
import 'perfect-scrollbar/css/perfect-scrollbar.css'

const updateScrollBar = el => {
    const railX = el.querySelector('.ps__rail-x')
    const _tbody = el //el为表格容器
        // 如果table内部还有滚动条的话需要加上_tbody.scrollTop
        // 视口的高度-tbody基于视口的top值-横向滚动条容器的高度
    const _top = window.innerHeight - _tbody.getBoundingClientRect().top - railX.clientHeight - 2
    const _left = window.innerWidth - _tbody.getBoundingClientRect().width
    const tableWidth = _tbody.getBoundingClientRect().width - (window.innerWidth - _tbody.getBoundingClientRect().width)
    railX.style.top = `${_top}px`
    railX.style.opacity = '1'
    railX.style.display = 'block'
    railX.style.position = 'absolute'
    railX.style.zIndex = '999'

    // railX.style.width = tableWidth + 'px'

}

// 使用插件创建滚动条
const el_scrollBar = el => {
    // 在元素上加点私活,名字随便取,确保不会和已有属性重复即可,取名叫做_ps_
    if (el._ps_ instanceof PerfectScrollbar) {
        el._ps_.update()
    } else {
        // el上挂一份属性
        el._ps_ = new PerfectScrollbar(el, {
            suppressScrollX: false,
            suppressScrollY: true //y方向禁止
        })
    }
}

let isScrolling = false
let _scrollHander = null
let _resizeHander = null

// 自定义vue指令
const directive = {
    // inserted(初次创建dom)获取使用自定义指令处的dom
    inserted(el) {
        el = document.querySelector('.el-table__body-wrapper')
        if (!el) {
            return console.warn('未发现className为el-table__body-wrapper的dom')
        }
        // 判断其样式是否存在position,并且position为"fixed","absolute",或"relative"
        // 如果不符合条件,抛个错误,当然你也可以抛个警告顺便给其position自动加上"relative"
        // 为什么要这样做呢,因为PrefectScrollbar实现原理就是dom注入两个div,一个是x轴一个是y轴,他们两的position都是absolute
        //对css稍有常识的人都知道，absolute是相对于所有父节点里设置了position属性的最近的一个节点来定位的，为了能够正确定位，我们要给其设置position属性
        const rules = ['fixed', 'absolute', 'relative']
        if (!rules.includes(window.getComputedStyle(el, null).position)) {
            console.error(`perfect-scrollbar所在的容器的position属性必须是以下之一：${rules.join('、')}`)
        }
        // el上挂一份属性
        el_scrollBar(el)
        updateScrollBar(el)

        //注册scroll和resize事件
        _scrollHander = () => {
            if (!isScrolling) {
                window.requestAnimationFrame(() => {
                    updateScrollBar(el)
                    isScrolling = false
                })
            }
            isScrolling = true
        }

        _resizeHander = () => {
                updateScrollBar(el)
            }
            // document.addEventListener('scroll', _scrollHander)
            // .el-scrollbar .el-scrollbar__wrap
        let dom = document.querySelector('.app-main .mainBox')
        dom.addEventListener('scroll', _scrollHander)
        window.addEventListener('resize', _resizeHander)
    },
    // 更新don的时候
    update(el, binding, vnode) {

        const { expression } = binding

        el = el.querySelector('.el-table__body-wrapper')
        if (!el) {
            return console.warn('未发现className为el-table__body-wrapper的dom')
        }

        // const handler = () => vnode.context[expression].apply()

        // // vnode.context其实就是vue实例,这里其实无需实例也直接用vue的静态方法
        // vnode.context.$nextTick(() => {
        //   try {
        el_scrollBar(el)
        updateScrollBar(el)
            //     if (expression) {
            //       handler()
            //     }·
            //   } catch (error) {
            //     console.error(error)
            //   }
            // })
    },

    unbind() {
        let dom = document.querySelector('.app-main .el-scrollbar .el-scrollbar__wrap')
        dom.removeEventListener('scroll', _scrollHander)
        window.removeEventListener('resize', _resizeHander)
    }
}

export default directive