<script lang="ts">
    import { onMount } from 'svelte';
    import type { Flight, Seat } from '$lib/types';
    import SeatButton from '$lib/components/SeatButton.svelte';
    import { goto } from '$app/navigation';

    // Saame lennu id
    export let data: { id: string };
    let id: string = data.id;

    let flight: Flight = null;
    let recommendedSeats = new Set<Seat>();
    let selectedSeats = new Set<Seat>();

    let recommendCount = 1;
    let wantWindow = false;
    let wantLegroom = false;
    let wantNearExit = false;
    let wantAdjacent = false;

    // Saame lennu ja istmed.
    onMount(async () => {
        const res = await fetch(`${import.meta.env.VITE_API_BASE}/flights/${id}`);
        const data = await res.json();

        flight = {
            ...data,
            departureTime: new Date(data.departureTime),
            seats: Array.isArray(data.seats)
                ? data.seats.map(seat => ({
                    id: seat.id,
                    seatNumber: seat.seatNumber,
                    isOccupied: seat.occupied,
                    isWindow: seat.window,
                    isLegroom: seat.legroom,
                    isNearExit: seat.nearExit
                }))
                : []
        };
    });

    // Meetod, mis soovitab istekohti sõltuvalt tahtele.
    async function recommendSeats() {
        const params = new URLSearchParams({
            count: recommendCount.toString()
        });

        if (wantWindow) params.append('isWindow', 'true');
        if (wantLegroom) params.append('isLegroom', 'true');
        if (wantNearExit) params.append('isNearExit', 'true');
        if (wantAdjacent) params.append('adjacentOnly', 'true');

        const res = await fetch(`${import.meta.env.VITE_API_BASE}/flights/${flight.id}/seats/recommendations?${params}`);
        const data = await res.json();

        recommendedSeats = new Set(data.map(seat => ({
            id: seat.id,
            seatNumber: seat.seatNumber,
            isOccupied: seat.occupied,
            isWindow: seat.window,
            isLegroom: seat.legroom,
            isNearExit: seat.nearExit
        })));
    }

    // Meetod, mis uuendab andmebaasis istmed võetuks ja suunab kodulehele
    async function confirmSelection() {
        if (selectedSeats.size === 0) return;

        const seatIDs = Array.from(selectedSeats).map(seat => seat.id);

        try {
            const response = await fetch(`${import.meta.env.VITE_API_BASE}/flights/${flight.id}/seats`, {
                method: 'PUT',
                headers: {
                    'Content-Type': 'application/json'
                },
                body: JSON.stringify(seatIDs)
            });

            if (!response.ok) {
                throw new Error('Server error');
            }

            goto('/');
        } catch (err) {
            console.error(err);
        }
    }

    // Funktsioon, mis toggleb istme valitust
    function toggleSeat(seat: Seat) {
        if (selectedSeats.has(seat)) {
            selectedSeats.delete(seat);
        } else {
            selectedSeats.add(seat);
        }
        selectedSeats = new Set(selectedSeats); // Hoia istmed reaktiivsena
    }

    // Abifunktsioon, mis sorteerib kõik istmed rea järgi
    function groupSeatsByRow(seats: Seat[]) {
        const rows: Record<string, Seat[]> = {};
        seats.forEach(seat => {
            const rowNumber = seat.seatNumber.match(/\d+/)?.[0];
            if (!rows[rowNumber]) rows[rowNumber] = [];
            rows[rowNumber].push(seat);
        });

        return Object.entries(rows)
            .sort((a, b) => +a[0] - +b[0])
            .map(([_, seats]) =>
                seats.sort((a, b) => a.seatNumber.localeCompare(b.seatNumber))
            );
    }

    // Abifunktsioon, mis ütleb kas on ekstra jalaruumiga rida
    function isExtraLegroomRow(rowSeats: Seat[]) {
        return rowSeats.some(seat => seat.isLegroom);
    }

    // Abifunktsioon, mis ütleb kas on väljapääsu rida
    function isExitRow(rowSeats: Seat[], exitRows: number[]) {
        const rowNumber = parseInt(rowSeats[0].seatNumber);
        return exitRows.includes(rowNumber);
    }

    // Abifunktsioon, mis ütleb, kas istekoht on valitud
    function isSeatSelected(seat: Seat) {
        return selectedSeats.has(seat);
    }

    // Abifunktsioon, mis ütleb, kas istekoht on soovitatud
    function isSeatRecommended(seat: Seat) {
        return Array.from(recommendedSeats).some(s => s.id === seat.id);
    }
</script>

{#if flight}
    <div class="flex flex-col sm:flex-row gap-6 mb-6">
        <!-- Lennuinfo -->
        <div class="sm:w-1/2">
            <h1 class="text-2xl font-bold mb-2">
                {flight.from} → {flight.to}
            </h1>
            <p class="mb-1">Väljumine: {new Date(flight.departureTime).toLocaleString()}</p>
            <p>Hind: <strong>{flight.price} €</strong></p>
        </div>

        <!-- Soovituse vorm -->
        <div class="sm:w-1/2 rounded px-3 py-2 text-sm">
            <form on:submit|preventDefault={recommendSeats}>
                <div class="mb-2 flex items-center gap-2">
                    <label class="whitespace-nowrap font-semibold text-base">Mitu kohta?</label>
                    <input type="number" min="1" bind:value={recommendCount} class="border p-1 rounded w-full text-sm"/>
                </div>

                <div class="grid grid-cols-1 sm:grid-cols-2 gap-1 mb-2">
                    <label><input type="checkbox" bind:checked={wantWindow} /> Aknakoht</label>
                    <label><input type="checkbox" bind:checked={wantLegroom} /> Rohkem jalaruumi</label>
                    <label><input type="checkbox" bind:checked={wantNearExit} /> Väljapääsu lähedal</label>
                    <label><input type="checkbox" bind:checked={wantAdjacent} /> Kõrvuti</label>
                </div>

                <button class="bg-blue-500 text-white rounded px-3 py-1 hover:bg-sky-600 transition text-sm w-full">
                    Soovita Istekohti
                </button>
            </form>
        </div>
    </div>

    <div class="max-h-[600px] overflow-y-auto w-full flex justify-center border-t border-b">
        <div class="w-fit p-4">
            {#each groupSeatsByRow(flight.seats) as rowSeats}
                <div class="mb-4 flex flex-col items-center">
                    <!-- Ekstra jalaruumi märge -->
                    {#if isExtraLegroomRow(rowSeats)}
                        <div class="flex items-center mb-3">
                            <div class="w-20"></div>
                            <div class="text-sm text-black-600 font-bold text-center w-full">
                                Ekstra Jalaruum
                            </div>
                        </div>
                    {/if}

                    <div class="flex">
                        <!-- Väljapääsu märge vasakul -->
                        {#if isExitRow(rowSeats, flight.exitRows)}
                            <div class="w-20 text-sm text-red-600 font-semibold">VÄLJAPÄÄS</div>
                        {:else}
                            <div class="w-20"></div>
                        {/if}

                        <!-- Istmete paigutus -->
                        <div class="flex justify-center gap-x-2">
                            {#if flight.planeModel === 'LARGE'}
                                <div class="flex gap-x-1">
                                    {#each rowSeats.slice(0, 3) as seat}
                                        <SeatButton {seat} selected={isSeatSelected(seat)} recommended={isSeatRecommended(seat)} onToggle={toggleSeat} />
                                    {/each}
                                </div>
                                <div class="w-8"></div>
                                <div class="flex gap-x-1">
                                    {#each rowSeats.slice(3, 6) as seat}
                                        <SeatButton {seat} selected={isSeatSelected(seat)} recommended={isSeatRecommended(seat)} onToggle={toggleSeat} />
                                    {/each}
                                </div>
                                <div class="w-8"></div>
                                <div class="flex gap-x-1">
                                    {#each rowSeats.slice(6, 9) as seat}
                                        <SeatButton {seat} selected={isSeatSelected(seat)} recommended={isSeatRecommended(seat)} onToggle={toggleSeat} />
                                    {/each}
                                </div>
                            {:else}
                                <div class="flex gap-x-1">
                                    {#each rowSeats.slice(0, 3) as seat}
                                        <SeatButton {seat} selected={isSeatSelected(seat)} recommended={isSeatRecommended(seat)} onToggle={toggleSeat} />
                                    {/each}
                                </div>
                                <div class="w-8"></div>
                                <div class="flex gap-x-1">
                                    {#each rowSeats.slice(3, 6) as seat}
                                        <SeatButton {seat} selected={isSeatSelected(seat)} recommended={isSeatRecommended(seat)} onToggle={toggleSeat} />
                                    {/each}
                                </div>
                            {/if}
                        </div>
                    </div>
                </div>
            {/each}
        </div>
    </div>

    <div class="mt-3 flex flex-row gap-4 w-full">
        <div class="w-3/4 px-4 py-3 flex items-center justify-between flex-row max-h-23 gap-3 w-full">
            <!-- Valitud istmete kast -->
            <div class="w-1/2 bg-white border rounded-lg shadow-md px-4 py-3">
                <h2 class="text-base font-semibold mb-1">Valitud kohad</h2>
                {#if selectedSeats.size > 0}
                    <p class="text-blue-500 font-semibold text-sm">
                        {Array.from(selectedSeats).map(seat => seat.seatNumber).join(', ')}
                    </p>
                {:else}
                    <p class="text-gray-500 italic text-sm">Pole valitud</p>
                {/if}
            </div>

            <!-- Soovitatud istmete kast -->
            <div class="w-1/2 bg-white border rounded-lg shadow-md px-4 py-3">
                <h2 class="text-base font-semibold mb-1">Soovitatud kohad</h2>
                {#if recommendedSeats.size > 0}
                    <p class="text-green-600 font-semibold text-sm">
                        {Array.from(recommendedSeats).map(seat => seat.seatNumber).join(', ')}
                    </p>
                {:else}
                    <p class="text-gray-500 italic text-sm">Pole soovitatud</p>
                {/if}
            </div>
        </div>


        <!-- Kinnitamise nupp-->
        <div class="w-1/4 flex items-center">
            <button
                on:click={confirmSelection}
                class="w-full px-4 py-3 bg-blue-500 text-white rounded-lg cursor-pointer opacity-100 disabled:opacity-75 disabled:cursor-default hover:bg-blue-600 transition text-sm font-semibold"
                disabled={selectedSeats.size === 0}
            >
                Kinnita
            </button>
        </div>
    </div>

{:else}
    <p class="text-gray-500 italic animate-pulse">Laen...</p>
{/if}