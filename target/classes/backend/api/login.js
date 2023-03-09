function loginApi(data) {
  return $axios({
    'url': '/employee/login',
    'method': 'post',
    data
  })
}
// http://localhost:8080/employee/login
function logoutApi(){
  return $axios({
    'url': '/employee/logout',
    'method': 'post',
  })
}
