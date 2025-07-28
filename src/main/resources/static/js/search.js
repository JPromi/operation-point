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
                rows[i].classList[found ? 'remove' : 'add']('disabled');
            } else {
                rows[i].classList.add("disabled");
            }
        }

        const sortedRows = Array.from(rows).sort((a, b) => {
            const aDisabled = a.classList.contains("disabled");
            const bDisabled = b.classList.contains("disabled");

            if (aDisabled && !bDisabled) return 1;
            if (!aDisabled && bDisabled) return -1;
            return 0;
        });

        for (let i = 0; i < sortedRows.length; i++) {
            table.appendChild(sortedRows[i]);
        }
    });
}

searchTable();
