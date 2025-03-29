<script lang="ts">
    export let seat: Seat;
    export let selected: boolean;
    export let recommended: boolean = false;
    export let onToggle: (seat: Seat) => void;
</script>

<button
        on:click={() => onToggle(seat)}
        disabled={seat.isOccupied}
        class={`w-12 h-12 flex items-center justify-center rounded border text-sm transition
        ${seat.isOccupied
            ? 'bg-gray-300 cursor-default text-gray-600'
            : selected
                ? 'bg-blue-500 text-white cursor-pointer'
                : recommended
                    ? 'bg-green-100 text-black cursor-pointer'
                    : 'bg-white hover:bg-blue-100 cursor-pointer'}
    `}
>
    {seat.seatNumber}
</button>