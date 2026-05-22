import Application from "./application";

export const capitalize = (string) => string.charAt(0).toUpperCase() + string.slice(1);
export const getCookie = (name, cookies) => {
  const value = `; ${Application.isBrowser ? document.cookie : cookies}`;
  const parts = value.split(`; ${name}=`);
  if (parts.length === 2) return parts.pop().split(";").shift();
};
export const removeCookie = (name, cookies) => {
  if (cookies) {
    return cookies.replace(`${name}=`, "");
  } else {
    document.cookie = name + "=; Path=/; Expires=Thu, 01 Jan 1970 00:00:01 GMT;";
  }
};
