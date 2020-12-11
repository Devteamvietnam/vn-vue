import enquireJs from 'enquire.js'

export function timeFix () {
  const time = new Date()
  const hour = time.getHours()
  return hour <9?'Good morning': hour <= 11?'Good morning': hour <= 13?'Good afternoon': hour <20?'Good afternoon':'Good evening'
}

export function welcome () {
  const arr = ['Lets take a break','How do you feel today?','Do you want to some eat? ','I guess you may be tired']
  const index = Math.floor(Math.random() * arr.length)
  return arr[index]
}

export function isDef (v){
  return v !== undefined && v !== null
}

/**
 * Remove an item from an array.
 */
export function remove (arr, item) {
  if (arr.length) {
    const index = arr.indexOf(item)
    if (index > -1) {
      return arr.splice(index, 1)
    }
  }
}

export function isRegExp (v) {
  return _toString.call(v) === '[object RegExp]'
}

export function enquireScreen(call) {
  const handler = {
    match: function () {
      call && call(true)
    },
    unmatch: function () {
      call && call(false)
    }
  }
  enquireJs.register('only screen and (max-width: 767.99px)', handler)
}

const _toString = Object.prototype.toString
