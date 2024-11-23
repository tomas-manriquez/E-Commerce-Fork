import { ref } from "vue";

export const token = ref(localStorage.getItem("token"));

export const setToken = (value) => {
    token.value = value;
    if (value) {
        localStorage.setItem("token", value);
    } else {
        localStorage.removeItem("token");
    }
};
