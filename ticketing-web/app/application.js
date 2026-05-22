import Path from "./path";

const Application = {
  isBrowser: typeof window !== "undefined",

  isServer: typeof window === "undefined",

  homepage: Path.AllTickets,

  version: {
    major: 1,
    minor: 0,
  },

  availableLanguages: ["en", "hr"],

  defaultLanguage: "hr",

  serverDomain:
    process.env.NODE_ENV === "production" ? "http://www.test.com" : "http://localhost:8080",
};

export default Application;
