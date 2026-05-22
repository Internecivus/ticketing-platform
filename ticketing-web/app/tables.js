const Tables = {
  CategoryAssignedUsers: {
    columns: [{ field: "username" }],
  },
  Categories: {
    columns: [{ field: "name" }],
  },
  Tickets: {
    columns: [
      { field: "title" },
      { field: "status" },
      { field: "assignedUser" },
      { field: "priority" },
      { field: "category" },
    ],
  },
  Users: {
    columns: [{ field: "username" }, { field: "email" }],
  },
};

export default Tables;
