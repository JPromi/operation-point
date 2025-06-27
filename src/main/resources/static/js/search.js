function searchTable() {
    const input = document.getElementById("searchInput");

    input.addEventListener("keyup", function () {
        const filter = input.value.toLowerCase();
        const table = document.getElementById("dataTable");
        const rows = table.getElementsByTagName("tr");

        for (let i = 0; i < rows.length; i++) {
            const nameAttr = rows[i].getAttribute("data-name");

            if (nameAttr) {
                const name = nameAttr.toLowerCase();
                const found = name.includes(filter);
                rows[i].style.display = found ? "" : "none";
            } else {
                rows[i].style.display = "none";
            }
        }
    });
}

searchTable();
