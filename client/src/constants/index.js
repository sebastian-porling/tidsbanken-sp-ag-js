const API_URL = process.env.NODE_ENV === 'development' ? "http://localhost:3400/api/" : "/api/";
const SOCKET_URL = process.env.NODE_ENV === 'development' ? "http://localhost:4121" : ":4121";
const headers = (token) => {return {headers: {authorization: `Bearer ${token}`}}};

export {
    API_URL,
    SOCKET_URL,
    headers
}