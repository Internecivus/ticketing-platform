const Storage = new (class {
  LANGUAGE = "language";
  CURRENT_USER = "CURRENT_USER";

  set(name, value) {
    if (!value) {
      window.localStorage.removeItem(name);
    } else {
      window.localStorage.setItem(name, JSON.stringify(value));
    }
  }

  get(name) {
    return JSON.parse(window.localStorage.getItem(name));
  }
})();

export default Storage;
